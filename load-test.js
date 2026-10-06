import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 20,
  iterations: 70,
};

export default function () {
  const response = http.post(
      `http://localhost:8080/api/tickets/${__ENV.TICKET_ID}/issue`,

      JSON.stringify({
        userId: Number(__ENV.USER_ID),
      }),
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
  );

  check(response, {
    '발급 성공: HTTP 200': (r) => r.status === 202,
  });
}