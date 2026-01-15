import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 20 },  // ramp-up
        { duration: '1m', target: 20 },   // carga sustentada
        { duration: '30s', target: 0 },   // ramp-down
    ],
    thresholds: {
        http_req_failed: ['rate<0.01'],     // < 1% erros
    },
};

export default function () {
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

    const res = http.post(url, payload, params);

    check(res, {
        'status é 200': (r) => r.status === 200,
        'tempo aceitável': (r) => r.timings.duration < 500,
    });

    sleep(1);
}
