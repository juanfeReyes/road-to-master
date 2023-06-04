import { Transaction } from "@/model/Transaction"
import { User } from "@/model/User"
import { useEffect, useState } from "react"
import { DynamicTable } from "../shared/DynamicTable"
import { useTranslation } from "next-i18next"

interface UserDetailProps{
  user: User,
  deposits: Transaction[]
  withdrawals: Transaction[]
}


export const UserDetail = ({user, deposits, withdrawals} : UserDetailProps) => {
  const { t } = useTranslation("common");
  const [transactions, setTransactions] = useState<Transaction[]>([])

  useEffect(() => {
    const joinTransactions = deposits.concat(withdrawals)
    joinTransactions.sort((a: Transaction, b: Transaction) => {return new Date(b.date).getTime() - new Date(a.date).getTime()} )
    setTransactions(joinTransactions)
  }, [])

  const transactionHeaders = [
    t('labels.origin'),
    t('labels.destination'),
    t('labels.amount'), t('labels.date')]

  const buildUserDetailTableRow = (row: Transaction) => {

    return (
      <tr>
        <td className="px-2 py-1">{row.origin}</td>
        <td className="px-2 py-1">{row.destination}</td>
        <td className="px-2 py-1">{row.amount}</td>
        <td className="px-2 py-1">{new Date(row.date).toISOString()}</td>
      </tr>
    );
  };

  return (
    <>
      <h2>{user.name}</h2>
      <p>{user.username}</p>
      <p>{user.email}</p>
      <div>
        <DynamicTable
          tableTitle={t("labels.transactions.title")}
          headers={transactionHeaders}
          body={transactions}
          buildBodyRow={buildUserDetailTableRow}
        />
      </div>
    </>
  )
}
