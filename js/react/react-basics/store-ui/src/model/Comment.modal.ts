import { User } from "./User.model"

export interface Comment {
  id: string
  text: string
  rating: number
  author: string
  expand: {author: User}
}
