import { Bank } from "@/model/Bank";
import { useTranslation } from "next-i18next";

interface BankListItemProps {
  bank: Bank;
}

export const BankListItem = ({ bank }: BankListItemProps) => {
  const { t } = useTranslation("common");

  return (
    <div className="flex items-center odd:bg-white even:bg-slate-50 hover:bg-blue-400">
      <img src={bank.imageUrl} className="w-1/6 p-3 rounded-3xl" />
      <div>
        <h3 className="text-xl">{bank.name}</h3>
        <div className="flex items-center">
          <div className="font-bold pr-3">{t("labels.location")}: </div>
          <p>
            {bank.country}, {bank.city}
          </p>
        </div>
        <div className="flex items-center">
          <div className="font-bold pr-3">{t("labels.address")}: </div>
          <p>{bank.address}</p>
        </div>
      </div>
    </div>
  );
};
