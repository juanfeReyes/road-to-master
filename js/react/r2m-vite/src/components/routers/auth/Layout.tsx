import { Outlet } from "react-router"

export const Home = () => {

  return (<div>
    <h2>Authentication</h2>
    <Outlet />
  </div>)
}

export default Home