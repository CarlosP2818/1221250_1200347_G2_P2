import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';

// Carregar livros do JSON (SharedArray retorna objetos congelados)
const book_load_test = new SharedArray('books', function () {
    return JSON.parse(open('./books.json'));
});

export let options = {
    stages: [
        { duration: '30s', target: 10 },  // subir VUs
        { duration: '1m', target: 10 },   // manter VUs
        { duration: '30s', target: 0 },   // descer VUs
    ],
};

// Função simples para gerar ISBN-13 válido aleatório
function generateIsbn13() {
    let digits = [];
    for (let i = 0; i < 12; i++) {
        digits.push(Math.floor(Math.random() * 10));
    }

    let sum = 0;
    for (let i = 0; i < 12; i++) {
        sum += i % 2 === 0 ? digits[i] : digits[i] * 3;
    }

    let checkDigit = (10 - (sum % 10)) % 10;
    digits.push(checkDigit);

    return digits.join('');
}

export default function () {
    // 1. Login
    let loginRes = http.post('http://localhost:8080/api/public/login', JSON.stringify({
        username: 'maria@gmail.com',
        password: 'Mariaroberta!123'
    }), { headers: { 'Content-Type': 'application/json' } });

    check(loginRes, { 'login ok': (r) => r.status === 200 });

// Pega o token do header Authorization
    let tokenHeader = loginRes.headers['Authorization']; // <- aqui vem "Bearer <token>"
    if (!tokenHeader) {
        throw new Error('Token não encontrado no header Authorization');
    }

// Configura headers com Authorization
    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${tokenHeader}`,
        }
    };

    const bookOriginal = book_load_test[Math.floor(Math.random() * book_load_test.length)];
    const book = JSON.parse(JSON.stringify(bookOriginal)); // cria um clone mutável

    // 3. Adiciona ISBN aleatório
    book.isbn = generateIsbn13();

// Exemplo de uso
    let resCreate = http.post(
        'http://localhost:8087/api/books',
        JSON.stringify(book),
        params
    );

    check(resCreate, { 'book created': (r) => r.status === 201 || r.status === 409 });


    let resGet = http.get(
        `http://localhost:8087/api/books/${book.isbn}`,
        params
    );

    check(resGet, { 'book fetched': (r) => r.status === 200 || r.status === 404 });

    sleep(1);
}
