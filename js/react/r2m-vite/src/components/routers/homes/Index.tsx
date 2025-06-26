import { HttpMethods } from "msw"
import type { Home } from "../../../mocks/handlers";
import { FaRegEye } from "react-icons/fa";
import { IoIosAddCircleOutline } from "react-icons/io";
import { Link, useNavigate } from "react-router";
import type { Route } from "./+types/Index";
import { CiEdit } from "react-icons/ci";


export async function clientLoader() {
  const res = await fetch('https://r2m.api/homes', { method: HttpMethods.GET })
  const homes: Home[] = await res.json()
  return { homes: homes };
}

export const Index = ({ loaderData }: Route.ComponentProps) => {
  const { homes } = loaderData
  const navigate = useNavigate();

  const handleGoToFormEdit = (id: string) => {
    navigate(`/homes/form/${id}`)
  }

  return (<div>
    <h2>Homes</h2>
    <div>
      <Link to={'/homes/form'}><IoIosAddCircleOutline />Register Home</Link>
    </div>
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Adress</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        {homes.map(home => (<tr id={home.id}>
          <td>{home.name}</td>
          <td>{home.address}</td>
          <th><button><FaRegEye /></button><button><CiEdit onClick={() => handleGoToFormEdit(home.id)} /></button></th>
        </tr>))}
      </tbody>
    </table>

  </div>)
}

export default Index
