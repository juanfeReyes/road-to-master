import http from 'k6/http';
import { check } from 'k6'

export function getWarehouse (){
  const url = 'http://host.docker.internal:8080/warehouse/dff8f08b-7d06-49e5-8b98-17ce7ab46061';

  const res = http.get(url);
  
  check(res, {
    "Get warehouse was 200": (res) => res.status === 200
  })
}