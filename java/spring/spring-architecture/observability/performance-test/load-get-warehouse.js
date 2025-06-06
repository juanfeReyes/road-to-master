import { average_loader } from './loaders/average-loader.js';
import { getWarehouse } from './feature/get-warehouse.js';

export const options = {
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<50']
  },
  scenarios: {
    average_load: average_loader()
  }
}

export default function () {
  getWarehouse()
}


