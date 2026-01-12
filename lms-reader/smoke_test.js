import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 1, // Apenas 1 utilizador para não criar conflitos de números sequenciais no mesmo milissegundo
    duration: '5s',
};

export default function () {
    const url = __ENV.READERS_URL || 'http://localhost:8090/api/readers';

    // Gerar um username único usando o timestamp atual em milissegundos
    const uniqueId = Date.now();
    const username = `miguel_${uniqueId}@exemplo.com`;

    const payload = JSON.stringify({
        "username": username,
        "password": "SenhaSegura123",
        "fullName": "Carlos Miguel Pereira",
        "birthDate": "1995-05-20",
        "phoneNumber": "912345678",
        "gdpr": true,
        "marketing": false,
        "thirdParty": true,
        "interestList": ["Ficção", "Tecnologia"]
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    // Execução do POST
    const res = http.post(url, payload, params);

    // Validação
    check(res, {
        'status é 202 (Accepted)': (r) => r.status === 202,
        'corpo está vazio (conforme o controller)': (r) => r.body === null || r.body.length === 0,
    });

    console.log(`Tentativa de criar leitor: ${username} - Status: ${res.status}`);

    sleep(1);
}