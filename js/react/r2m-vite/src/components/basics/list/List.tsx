import { faker } from '@faker-js/faker'
import './List.css'

interface Transaction {
  id: string,
  type: string,
  currency: string,
  symbol: string,
  amount: string,
  date: string
}

const buildTransaction = (): Transaction => ({
  id: faker.string.uuid(),
  type: faker.finance.transactionType(),
  currency: faker.finance.currencyCode(),
  symbol: faker.finance.currencySymbol(),
  amount: faker.finance.amount(),
  date: faker.date.recent().toISOString()
})

interface ListProps {
  dataCount: number
}

export const List = ({ dataCount }: ListProps) => {

  const data =faker.helpers.multiple(buildTransaction, { count: dataCount })

  return (
    <table >
      <thead>
        <Headers {...data[0]} />
      </thead>
      <tbody>
        {data.map((transaction) => (
          <ListItem key={transaction.id} {...transaction} />
        ))}
      </tbody>
    </table>
  )
}

const Headers = (transaction: Transaction) => {
  const capitalizeString = (val: string) => val.charAt(0).toUpperCase() + val.slice(1)

  return (
    <tr>
      {Object.keys(transaction).map((col) => <th key={col}>{capitalizeString(col)}</th>)}
    </tr>
  )
}

const ListItem = (transaction: Transaction) => {

  return (
    <tr>
      {Object.values(transaction).map((col) => <td key={`${transaction.id}-${col}`}>{col}</td>)}
    </tr>
  )
}
