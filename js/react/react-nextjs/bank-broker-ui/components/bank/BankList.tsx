import { Bank } from "@/model/Bank";
import { BankListItem } from "./BankListItem/BankListItem";
import { useTranslation } from "next-i18next";
import Link from "next/link";

interface BankListProps {
  banks: Bank[];
}

export const BankList = ({ banks }: BankListProps) => {
  const { t } = useTranslation("common");

  return (
    <>
      <h2 className="text-xl">{t("bankList.header")}</h2>
      <div className="">
        {banks.map((bank) => (
          <Link key={bank.id} href={`banks/${bank.id}`}>
            <BankListItem key={bank.id} bank={bank} />
          </Link>
        ))}
      </div>
    </>
  );
};
