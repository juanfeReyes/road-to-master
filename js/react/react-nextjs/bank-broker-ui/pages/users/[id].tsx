import { NavigationBar } from "@/components/navigationBar/NavigationBar"
import { UserDetail } from "@/components/user/UserDetail"
import { Transaction } from "@/model/Transaction";
import { User } from "@/model/User";
import { getUserDeposits, getUserWithdrawals } from "@/services/TransactionService";
import { getUserDetail } from "@/services/UserService";
import { GetServerSidePropsContext } from "next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";

interface UserDetailPageProps{
  user: User,
  deposits: Transaction[]
  withdrawals: Transaction[]
}

// TODO: solve type error
export const getServerSideProps = async ({locale, params}: GetServerSidePropsContext) => {
  const userId = params.id;
  const userDetails = await getUserDetail(userId)
  const deposits = await getUserDeposits(userId)
  const withdrawals = (await getUserWithdrawals(userId))
    .map(transaction => ({...transaction, amount: transaction.amount*(-1)}))

  return {
    props: {
      user: JSON.parse(JSON.stringify(userDetails)),
      deposits: deposits,
      withdrawals: withdrawals,
      ...(await serverSideTranslations(locale!, ["common"])),
    },
  }
} 

const UserDetailPage = ({user, deposits, withdrawals}: UserDetailPageProps) => {

  return (
    <>
      <NavigationBar>
        <UserDetail user={user} deposits={deposits} withdrawals={withdrawals}/>
      </NavigationBar>
    </>
  )
}


export default UserDetailPage

