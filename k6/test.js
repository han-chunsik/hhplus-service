import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 100, // 가상 사용자 수
    duration: '30s', // 테스트 지속 시간
};

export default function () {
    const userId = `${__VU}`; // __VU는 현재 가상 사용자 번호
    const seatId = '107'; // 좌석 ID

    // 예약 요청을 보냄
    const url = `http://host.docker.internal:8080/api/v1/reservation/temporary`;
    const payload = JSON.stringify({
        seatId: seatId,
        userId: userId,
    });

    const reservationResponse = http.post(url, payload, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `test`, // 발급받은 토큰을 헤더에 포함
        },
    });

    check(reservationResponse, {
        '예약 요청 성공': (r) => r.status === 200,
    });
}
