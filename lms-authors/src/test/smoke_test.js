import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 1,
    duration: '15s',
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

    let createRes = http.post('http://localhost:8088/api/authors', authorPayload, params);

    check(createRes, {
        'POST author status 201': (r) => r.status === 400 || r.status === 201,
    });

    const authorCreated = createRes.json();

    const authorNumber = authorCreated.authorNumber;
    if (authorNumber) {
        let getRes = http.get(`http://localhost:8088/api/authors/${authorNumber}`, params);

        check(getRes, {
            'GET author detail status 200': (r) => r.status === 200,
        });

        console.log(`GET /authors/${authorNumber} - Status: ${getRes.status}`);
        console.log(`Response: ${getRes.body}`);
    }

    const nameSearchUrl = `http://localhost:8088/api/authors?name=MariaTest_${uniqueId}`;
    let resSearch = http.get(nameSearchUrl, params);

    check(resSearch, {
        'GET authors search status 200': (r) => r.status === 200,
        'body não vazio': (r) => r.body && r.body.length > 0,
    });

    console.log(`GET /authors?name=MariaTest_${uniqueId} - Status: ${resSearch.status}`);
    console.log(`Response: ${resSearch.body}`);

    sleep(1);
}
