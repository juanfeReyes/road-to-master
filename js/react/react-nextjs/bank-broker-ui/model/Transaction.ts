import { User } from "./User"


export interface Transaction {
  id: string
  origin: string
  destination: string
  date: Date
  amount: number
  expand: {
    origin: User
    destination: User
  }
}
