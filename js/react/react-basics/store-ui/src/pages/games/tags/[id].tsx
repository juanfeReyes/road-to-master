import { GamesList } from '@/components/GamesList/GamesList'
import { getGames } from '@/services/GameService'
import { Game } from '@/model/Game.model'
import { GetServerSideProps } from 'next'
import { HomePage } from '@/pages/HomePage'
import { ProtectedRoute } from '@/components/Router/ProtectedRoute'


export const getServerSideProps: GetServerSideProps = async (context) => {
  const filters = { title: '', tag: context.params?.id as string };
  const games = await getGames(filters)
  return {
    props: { filters: filters, response: JSON.parse(JSON.stringify(games)) }
  }
}

export default function GameListByTagPage(props: { response: Game[], filters: { [key: string]: string } }) {


  return (
    <>
      <div className="App">
        <ProtectedRoute>
          <HomePage>
            <GamesList response={props.response} filters={props.filters} />
          </HomePage>
        </ProtectedRoute>
      </div>

    </>
  )
}
