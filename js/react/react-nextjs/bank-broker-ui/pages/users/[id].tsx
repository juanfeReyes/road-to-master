import { ProtectPage } from "@/components/auth/ProtectPage";
import { NavigationBar } from "@/components/navigationBar/NavigationBar";
import { UserDetail } from "@/components/user/UserDetail";
import { Transaction } from "@/model/Transaction";
import { User } from "@/model/User";
import {
  getUserDeposits,
  getUserWithdrawals,
} from "@/services/TransactionService";
import { getUserAvatarImageUrl, getUserDetail } from "@/services/UserService";
import { GetServerSidePropsContext } from "next";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";

interface UserDetailPageProps {
  user: User;
  deposits: Transaction[];
  withdrawals: Transaction[];
  userAvatarUrl: string;
}

// TODO: solve type error
export const getServerSideProps = async ({
  locale,
  params,
}: GetServerSidePropsContext) => {
  const userId = params!.id as string;
  const userDetails = await getUserDetail(userId);
  const deposits = await getUserDeposits(userId);
  const withdrawals = (await getUserWithdrawals(userId)).map((transaction) => ({
    ...transaction,
    amount: transaction.amount * -1,
  }));
  const avatarUrl = await getUserAvatarImageUrl(userId);
  return {
    props: {
      user: JSON.parse(JSON.stringify(userDetails)),
      deposits: deposits,
      withdrawals: withdrawals,
      userAvatarUrl: avatarUrl,
      ...(await serverSideTranslations(locale!, ["common"])),
    },
  };
};

const UserDetailPage = ({
  user,
  deposits,
  withdrawals,
  userAvatarUrl,
}: UserDetailPageProps) => {
  return (
    <>
      <ProtectPage>
        <NavigationBar>
          <UserDetail
            user={user}
            deposits={deposits}
            withdrawals={withdrawals}
            userAvatarUrl={userAvatarUrl}
          />
        </NavigationBar>
      </ProtectPage>
    </>
  );
};

export default UserDetailPage;
