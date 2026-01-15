import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '30s', target: 10 },  // subir VUs
        { duration: '1m', target: 10 },   // manter VUs
        { duration: '30s', target: 0 },   // descer VUs
    ],
};

export default function () {
    let loginRes = http.post('http://localhost:8080/api/public/login', JSON.stringify({
        username: 'maria@gmail.com',
        password: 'Mariaroberta!123'
    }), { headers: { 'Content-Type': 'application/json' } });

    check(loginRes, { 'login ok': (r) => r.status === 200 });

    let tokenHeader = loginRes.headers['Authorization'];
    if (!tokenHeader) {
        throw new Error('Token não encontrado no header Authorization');
    }

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${tokenHeader}`,
        }
    };

    const uniqueId = Date.now();
    const authorPayload = JSON.stringify({
        name: `MariaTest_${uniqueId}`,
        bio: 'Bio de teste'
    });

    const res = http.get(`http://localhost:8089/api/genres/top5`, params);

    check(res, {
        '200': (r) => r.status === 200
    });

    sleep(1);
}
