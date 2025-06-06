import { GamesList } from '@/components/GamesList/GamesList'
import { getGames } from '@/services/GameService'
import { Game } from '@/model/Game.model'
import { GetServerSideProps } from 'next'
import { HomePage } from '../HomePage'


export const getServerSideProps: GetServerSideProps = async () => {
  const games = await getGames()
  return {
    props: {response: JSON.parse(JSON.stringify(games))}
  }
}

export default function Home(props : {response: Game[], filters: {[key: string]: string}}) {
  return (
    <>
    <div className="App">
        <HomePage>
          <GamesList response={props.response} filters={props.filters}/>
        </HomePage>
    </div>
     
    </>
  )
}
