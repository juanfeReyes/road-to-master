
import { faker } from '@faker-js/faker'
import {http, HttpResponse} from 'msw'

export interface Home {
  id: string,
  name: string,
  address: string
}

const buildHome = () =>  ({
  id: faker.string.uuid(),
  name: faker.commerce.productName(),
  address: faker.location.streetAddress()
})
const homes = faker.helpers.multiple(buildHome, {count: 10})

export const handlers = [
  http.get('https://r2m.api/homes', () => {
    return HttpResponse.json(homes)
  }),
  http.get<{id: string}>('https://r2m.api/homes/:id', ({params}) => {
    const {id} = params
    const home = homes.find((h) => h.id === id)
    return HttpResponse.json(home)
  })
]
