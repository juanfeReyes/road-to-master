import { Form, useNavigate } from "react-router"
import type { Route } from "./+types/HomeForm"
import { CiCircleCheck } from "react-icons/ci"
import { IoMdArrowBack } from "react-icons/io"
import type { Home } from "../../../mocks/handlers"
import { useState, type ChangeEvent } from "react"

export async function clientAction({ request }: Route.ClientActionArgs) {
  const formData = await request.formData()
  const homeReq = {
    name: formData.get('name'),
    address: formData.get('address')
  }

  console.log('create home: ', homeReq)
  return homeReq
}

export const clientLoader = async ({ params }: Route.ClientLoaderArgs) => {
  const { id } = params
  if (id) {
    const res = await fetch(`https://r2m.api/homes/${id}`)
    const home: Home = await res.json()
    return home
  }
  return {
    name: '',
    address: ''
  } as Home
}

export const HomeForm = ({ actionData, loaderData }: Route.ComponentProps) => {
  const navigate = useNavigate()

  const [home, setHome] = useState(loaderData)

  const handleGoBack = () => {
    navigate(-1)
  }

  const handleOnChangeInput = (event: ChangeEvent<HTMLInputElement>) => {
    setHome({...home, [event.target.name]: event.target.value})
  }

  return (<div>
    <h1>Register Home</h1>
    <button onClick={handleGoBack}><IoMdArrowBack />back to homes</button>
    <Form method="post">
      <label htmlFor="name">Name: </label>
      <input type="text" name="name" value={home?.name} onChange={handleOnChangeInput}/>
      <label htmlFor="address">Address: </label>
      <input type="text" name="address" value={home?.address} onChange={handleOnChangeInput} />
      <button type="submit"><CiCircleCheck />Submit</button>
    </Form>
    {
      actionData && (<div>
        <h3>Action data</h3>
        <p>name: {actionData?.name}</p>
        <p>address: {actionData?.address}</p>
      </div>)
    }

  </div>)
}

export default HomeForm
