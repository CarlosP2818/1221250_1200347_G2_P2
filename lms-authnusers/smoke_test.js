import http from 'k6/http';
import { check, sleep } from 'k6';

// 1. Configuração do Teste
export const options = {
    vus: 5,           // 5 utilizadores virtuais simultâneos
    duration: '10s',  // duração total do teste de fumo
    thresholds: {
        http_req_failed: ['rate<0.01'],   // menos de 1% de erros
    },
};

export default function () {
    // 2. Usar o URL vindo do Jenkins ou fallback para localhost:8080
    // No teu caso, o Jenkins passará http://localhost:30081/api/public/login
    const url = __ENV.AUTH_URL || 'http://localhost:8080/api/public/login';

    const payload = JSON.stringify({
        username: 'maria@gmail.com',
        password: 'Mariaroberta!123',
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    // 3. Execução do Pedido
    const res = http.post(url, payload, params);

    // 4. Validação dos Resultados
    check(res, {
        'status é 200': (r) => r.status === 200,
        'tem token ou sucesso': (r) => r.body.includes('token') || r.status === 200,
    });

    sleep(1);
}