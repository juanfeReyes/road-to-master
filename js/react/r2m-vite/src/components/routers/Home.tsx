
import type { Route } from '../../../.react-router/types/src/components/routers/+types/Home'

export const clientLoader = async () => {
  // request to get data
  return {
    title: 'Router test',
    version: 1.1
  }
}


const Home = ({loaderData}: Route.ComponentProps) => {

  return (<div>
    <h2>Home</h2>
  </div>)
}


export default Home