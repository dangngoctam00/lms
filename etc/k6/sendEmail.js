import http from 'k6/http';

// export const options = {
//     vus: 50,
//     duration: '30s',
// };


export const options = {
    thresholds: {
        http_req_failed: ['rate<0.01'], // http errors should be less than 1%
        http_req_duration: ['p(95)<500'], // 95% of requests should be below 200ms,

    },
    scenarios: {
        my_scenario: {
            executor: 'constant-arrival-rate',
            duration: '10s', // total duration
            preAllocatedVUs: 2000, // to allocate runtime resources     preAll

            rate: 500, // number of constant iterations given `timeUnit`
            timeUnit: '1s',
        },
    },
};

export default function () {

    // const url = 'http://localhost:8081/v1/send';
    const url = 'http://localhost:8080/api/v1/send';

    const payload = JSON.stringify({
        "from": {
            "name": "Dang Ngoc Tam",
            "address": "dnt@gmail.com"
        },
        "to": [
            {
                "name": "Dang Ngoc Tam",
                "address": "dnt@gmail.com"
            }
        ],
        "subject": "Welcome to LMS",
        "content": "You've registered LMS application successfully",
        "attachments": []
    });


    const params = {

        headers: {

            'Content-Type': 'application/json',

        },

    };


    http.post(url, payload, params);

}