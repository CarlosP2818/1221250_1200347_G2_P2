package pt.psoft.g1.psoftg1.readermanagement.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;
import pt.psoft.g1.psoftg1.readermanagement.model.PhoneNumber;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.util.List;

/**
 * Brief guide:
 * <a href="https://www.baeldung.com/mapstruct">https://www.baeldung.com/mapstruct</a>
 * */
@Mapper(componentModel = "spring", uses = {ReaderService.class})
public abstract class ReaderMapper {

    //@Mapping(target = "username", source = "username")
    //@Mapping(target = "password", source = "password")
    //@Mapping(target = "name", source = "fullName")
    //public abstract Reader createReader(CreateReaderRequest request);

    @Mapping(target = "photo", source = "photoURI")
    @Mapping(target = "interestList", source = "interestList")
    @Mapping(target = "birthDate", source = "request.birthDate", qualifiedByName = "stringToBirthDate")
    @Mapping(target = "phoneNumber", source = "request.phoneNumber", qualifiedByName = "stringToPhoneNumber")
    public abstract ReaderDetails createReaderDetails(int readerNumber, String reader, CreateReaderRequest request, String photoURI, List<String> interestList);

    @Named("stringToBirthDate")
    protected BirthDate stringToBirthDate(String value) {
        return value == null ? null : new BirthDate(value);
    }

    @Named("stringToPhoneNumber")
    protected PhoneNumber stringToPhoneNumber(String value) {
        return value == null ? null : new PhoneNumber(value);
    }
}
