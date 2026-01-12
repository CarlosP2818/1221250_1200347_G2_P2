import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 1,
    duration: '10s',
};

export default function () {

    let loginRes = http.post('http://localhost:8080/api/public/login', JSON.stringify({
        username: 'maria@gmail.com',
        password: 'Mariaroberta!123'
    }), { headers: { 'Content-Type': 'application/json' } });

    check(loginRes, { 'login ok': (r) => r.status === 200 });

    let tokenHeader = loginRes.headers['Authorization'];
    if (!tokenHeader) throw new Error('Token não encontrado no header Authorization');

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${tokenHeader}`,
        }
    };

    const genreName = 'Romance';
    const res = http.get(`http://localhost:8089/api/genre/${genreName}`, params);

    check(res, {
        'status é 200': (r) => r.status === 200,
        'corpo presente': (r) => r.body != null
    });

    sleep(1);
}
