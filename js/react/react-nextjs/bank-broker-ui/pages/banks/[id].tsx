import { BankDetail } from "@/components/bank/BankDetails";
import { NavigationBar } from "@/components/navigationBar/NavigationBar";
import { Bank } from "@/model/Bank";
import { getBankDetails, getBanks } from "@/services/BankService";
import { GetServerSidePropsContext } from "next";
import { getServerSession } from "next-auth/next";
import { getSession, useSession } from "next-auth/react";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { authOptions } from "../api/auth/[...nextauth]";
import { useRouter } from "next/router";
import { ProtectPage } from "@/components/auth/ProtectPage";

interface BankDetailPageProps {
  bank: Bank;
}

export const getStaticPaths = async () => {
  const banks = await getBanks();

  const paths = banks.map((bank) => ({ params: { id: bank.id } }));
  return { paths: paths, fallback: true };
};

// TODO: solve type error
export const getStaticProps = async ({
  params,
  locale,
}: GetServerSidePropsContext) => {
  const bank = await getBankDetails(params?.id as string);

  return {
    props: {
      bank: JSON.parse(JSON.stringify(bank)),
      ...(await serverSideTranslations(locale!, ["common"])),
    },
    revalidate: 10,
  };
};

const BankDetailPage = ({ bank }: BankDetailPageProps) => {
  return (
    <>
      <ProtectPage>
        <NavigationBar>
          <BankDetail bank={bank} />
        </NavigationBar>
      </ProtectPage>
    </>
  );
};

export default BankDetailPage;
