import { BankList } from "@/components/bank/BankList";
import { getBanks } from "@/services/BankService";
import { Bank } from "@/model/Bank";
import { serverSideTranslations } from "next-i18next/serverSideTranslations";
import { NavigationBar } from "@/components/navigationBar/NavigationBar";

interface HomeProps {
  banks: Bank[];
}

export async function getStaticProps({ locale }) {
  const banks = await getBanks();
  return {
    props: {
      banks: JSON.parse(JSON.stringify(banks)),
      ...(await serverSideTranslations(locale, ["common"])),
    },
    revalidate: 30,
  };
}

export default function Home({ banks }: HomeProps) {
  return (
    <>
      <NavigationBar>
        <BankList banks={banks} />
      </NavigationBar>
    </>
  );
}
