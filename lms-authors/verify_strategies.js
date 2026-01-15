import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 1,
    duration: '2s',
};

export default function () {

    // --- ENV (vindos do Jenkins) ---
    const AUTH_URL = __ENV.AUTH_URL || 'http://localhost:8080';
    const AUTHORS_URL = __ENV.AUTHORS_URL || 'http://localhost:8088';
    const USERNAME = __ENV.USERNAME || 'maria@gmail.com';
    const PASSWORD = __ENV.PASSWORD || 'Mariaroberta!123';
    const KILLSWITCH_ACTIVE = __ENV.KILLSWITCH_ACTIVE === true;

    // --- Login ---
    const loginRes = http.post(
        `${AUTH_URL}/api/public/login`,
        JSON.stringify({ username: USERNAME, password: PASSWORD }),
        { headers: { 'Content-Type': 'application/json' } }
    );

    check(loginRes, { 'login ok': (r) => r.status === 200 });

    const token = loginRes.headers['Authorization'];
    if (!token) {
        throw new Error('Token não encontrado no header Authorization');
    }

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        }
    };

    // --- Teste Kill Switch ---
    const res = http.get(
        `${AUTHORS_URL}/api/authors/TEST_KILLSWITCH`,
        params
    );

    if (KILLSWITCH_ACTIVE) {
        check(res, {
            'Kill Switch ativo → 503': (r) => r.status === 503,
        });
    } else {
        check(res, {
            'Kill Switch inativo → != 503': (r) => r.status !== 503,
        });
    }

    console.log(`KILLSWITCH_ACTIVE=${KILLSWITCH_ACTIVE}`);
    console.log(`Status recebido: ${res.status}`);
    console.log(`Body: ${res.body}`);

    sleep(1);
}
