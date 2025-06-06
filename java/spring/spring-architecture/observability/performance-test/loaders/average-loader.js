export function average_loader () {
  return {
    executor: "ramping-vus",
    stages: [
      { duration: '5s', target: 1},
      { duration: '20s', target: 5},
      { duration: '40s', target: 20},
      { duration: '10s', target: 5},
      { duration: '5s', target: 1},
    ]
  }
}