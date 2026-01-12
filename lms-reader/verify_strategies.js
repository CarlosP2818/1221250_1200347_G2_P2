import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 1,
    duration: '2s',
};

export default function () {
    // TARGET_URL vindo do Jenkins (ex: http://readers-blue:8090)
    const baseUrl = __ENV.TARGET_URL;
    const killswitchActive = __ENV.KILLSWITCH_ACTIVE === 'true';

    // --- 2. Testar Kill Switch (esperando 503) ---
    // Fazemos um pedido normal sem o header secreto
    const resKill = http.get(`${baseUrl}/api/readers`);

    if (killswitchActive) {
        check(resKill, {
            'Kill Switch Ativo: status é 503': (r) => r.status === 503,
        });
    } else {
        check(resKill, {
            'Kill Switch Inativo: status não é 503': (r) => r.status !== 503,
        });
    }

    sleep(1);
}