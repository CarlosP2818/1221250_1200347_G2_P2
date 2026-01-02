package pt.psoft.g1.psoftg1.readermanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.jpa.ReaderDetailsJpaTemp;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.jpa.SpringReaderDetailsJpaTempRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderNumber;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderDetailsTempRepository;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {
    private final ReaderRepository readerRepo;
    private final ReaderMapper readerMapper;
    private final ForbiddenNameRepository forbiddenNameRepository;
    private final PhotoRepository photoRepository;
    private final ReaderDetailsTempRepository springReaderDetailsJpaTempRepository;


    @Override
    @CacheEvict(value = {
            "readersByNumber",
            "readersByUsername",
            "readersByPhone",
            "topReaders",
            "readersAll",
            "readerSearch"
    }, allEntries = true)
    public ReaderDetails create(CreateReaderRequest request, String photoURI) {
        Iterable<String> words = List.of(request.getFullName().split("\\s+"));
        for (String word : words){
            if(!forbiddenNameRepository.findByForbiddenNameIsContained(word).isEmpty()) {
                throw new IllegalArgumentException("Name contains a forbidden word");
            }
        }

        List<String> stringInterestList = request.getInterestList();
        /*if(stringInterestList != null && !stringInterestList.isEmpty()) {
            request.setInterestList(this.getGenreListFromStringList(stringInterestList));
        }*/

        /*
         * Since photos can be null (no photo uploaded) that means the URI can be null as well.
         * To avoid the client sending false data, photoURI has to be set to any value / null
         * according to the MultipartFile photo object
         *
         * That means:
         * - photo = null && photoURI = null -> photo is removed
         * - photo = null && photoURI = validString -> ignored
         * - photo = validFile && photoURI = null -> ignored
         * - photo = validFile && photoURI = validString -> photo is set
         * */

        MultipartFile photo = request.getPhoto();
        if(photo == null && photoURI != null || photo != null && photoURI == null) {
            request.setPhoto(null);
        }

        int count = readerRepo.getCountFromCurrentYear();
        ReaderDetails rd = readerMapper.createReaderDetails(count+1, request.getReader(), request, photoURI, stringInterestList);

        readerRepo.save(rd);

        springReaderDetailsJpaTempRepository.findByReaderNumber(request.getReader()).ifPresent(springReaderDetailsJpaTempRepository::delete);

        return rd;
    }

    @Override
    public List<ReaderBookCountDTO> findTopByGenre(String genre, LocalDate startDate, LocalDate endDate){
        if(startDate.isAfter(endDate)){
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        Pageable pageableRules = PageRequest.of(0,5);
        return this.readerRepo.findTopByGenre(pageableRules, genre, startDate, endDate).getContent();
    }

    @Override
    @CacheEvict(value = {
            "readersByNumber",
            "readersByUsername",
            "readersByPhone",
            "topReaders",
            "readersAll",
            "readerSearch"
    }, allEntries = true)
    public ReaderDetails update(final String id, final UpdateReaderRequest request, final long desiredVersion, String photoURI){
        final ReaderDetails readerDetails = readerRepo.findByUserId(id)
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        List<String> stringInterestList = request.getInterestList();

         /*
         * Since photos can be null (no photo uploaded) that means the URI can be null as well.
         * To avoid the client sending false data, photoURI has to be set to any value / null
         * according to the MultipartFile photo object
         *
         * That means:
         * - photo = null && photoURI = null -> photo is removed
         * - photo = null && photoURI = validString -> ignored
         * - photo = validFile && photoURI = null -> ignored
         * - photo = validFile && photoURI = validString -> photo is set
         * */

        MultipartFile photo = request.getPhoto();
        if(photo == null && photoURI != null || photo != null && photoURI == null) {
            request.setPhoto(null);
        }

        readerDetails.applyPatch(desiredVersion, request, photoURI, stringInterestList);
        return readerRepo.save(readerDetails);
    }


    @Override
    @Cacheable(value = "readersByNumber", key = "#readerNumber")
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        return this.readerRepo.findByReaderNumber(readerNumber);
    }

    @Override
    @Cacheable(value = "readersByPhone", key = "#phoneNumber")
    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
        return this.readerRepo.findByPhoneNumber(phoneNumber);
    }

    @Override
    @Cacheable(value = "readersByUsername", key = "#username")
    public Optional<ReaderDetails> findByUsername(final String username) {
        return this.readerRepo.findByUsername(username);
    }


    @Override
    @Cacheable(value = "readersAll")
    public Iterable<ReaderDetails> findAll() {
        return this.readerRepo.findAll();
    }

    @Override
    @Cacheable(value = "topReaders", key = "#minTop")
    public List<ReaderDetails> findTopReaders(int minTop) {
        if(minTop < 1) {
            throw new IllegalArgumentException("Minimum top reader must be greater than 0");
        }

        Pageable pageableRules = PageRequest.of(0,minTop);
        Page<ReaderDetails> page = readerRepo.findTopReaders(pageableRules);
        return page.getContent();
    }

    @Override
    @CacheEvict(value = {
            "readersByNumber",
            "readersByUsername",
            "readersByPhone",
            "topReaders",
            "readersAll"
    }, allEntries = true)
    public Optional<ReaderDetails> removeReaderPhoto(String readerNumber, long desiredVersion) {
        ReaderDetails readerDetails = readerRepo.findByReaderNumber(readerNumber)
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        String photoFile = readerDetails.getPhoto().getPhotoFile();
        readerDetails.removePhoto(desiredVersion);
        Optional<ReaderDetails> updatedReader = Optional.of(readerRepo.save(readerDetails));
        photoRepository.deleteByPhotoFile(photoFile);
        return updatedReader;
    }

    @Override
    @Cacheable(value = "readerSearch", key = "#page.page + '_' + #query.username + '_' + #query.fullName + '_' + #query.phoneNumber")
    public List<ReaderDetails> searchReaders(pt.psoft.g1.psoftg1.shared.services.Page page, SearchReadersQuery query) {
        if (page == null)
            page = new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);

        if (query == null)
            query = new SearchReadersQuery("", "","");

        final var list = readerRepo.searchReaderDetails(page, query);

        if(list.isEmpty())
            throw new NotFoundException("No results match the search query");

        return list;
    }

    @Override
    public ReaderDetailsJpaTemp createTemp(CreateReaderRequest request, String photoURI, String correlationId) {
        int count = springReaderDetailsJpaTempRepository.getCountFromCurrentYear();
        ReaderDetailsJpaTemp rd = new ReaderDetailsJpaTemp(
                correlationId,
                count+1,
                request.getReader(),
                request.getBirthDate(),
                request.getPhoneNumber(),
                request.getGdpr(),
                request.getMarketing(),
                request.getThirdParty(),
                photoURI,
                request.getInterestList(),
                request.getUsername(),
                request.getPassword(),
                request.getFullName()
        );

        springReaderDetailsJpaTempRepository.save(rd);

        return rd;
    }
}
