import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 1,
    duration: '2s', // Teste rápido apenas para validação
};

export default function () {
    const baseUrl = __ENV.TARGET_URL; // Ex: http://users-green-users-1:8080

    // --- 1. Testar Dark Launch (com Header Secreto) ---
    const darkLaunchParams = {
        headers: { 'X-LMS-Beta-Access': 'true' },
    };
    const resDark = http.get(`${baseUrl}/api/admin/users/new-feature`, darkLaunchParams);

    check(resDark, {
        'Dark Launch: status é 200': (r) => r.status === 200,
    });

    // --- 2. Testar Kill Switch (esperando 503) ---
    const resKill = http.get(`${baseUrl}/api/admin/users`);

    check(resKill, {
        'Kill Switch: status é 503': (r) => r.status === 503,
    });

    sleep(1);
}