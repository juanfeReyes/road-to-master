import { Bank } from "@/model/Bank";
import { useTranslation } from "next-i18next";
import Image from "next/image";
import { DynamicTable } from "../shared/DynamicTable";
import { User } from "@/model/User";
import { useRouter } from "next/router";

interface BankDetailProps {
  bank: Bank;
}

export const BankDetail = ({ bank }: BankDetailProps) => {
  const { t } = useTranslation("common");
  const router = useRouter();

  const navigateToUserDetailPage = (row: User) => {
    router.push(`/users/${row.id}`);
  };

  const buildUserDetailTableRow = (row: User) => {
    return (
      <tr onClick={() => navigateToUserDetailPage(row)}>
        <td className="px-2 py-1">{row.name}</td>
        <td className="px-2 py-1">{row.email}</td>
      </tr>
    );
  };

  const userDetailHeaders = [t("labels.name"), t("labels.email"), ""];

  return (
    <div>
      <div className="flex items-start">
        <div>
          <Image
            className="p-3 rounded-3xl"
            src={bank.imageUrl}
            alt={"Picture for bank"}
            width={200}
            height={200}
          />
        </div>
        <div className="p-3">
          <h1>{bank.name}</h1>
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
      <div>
        <DynamicTable
          tableTitle={t("labels.users.title")}
          headers={userDetailHeaders}
          body={bank.expand.users}
          buildBodyRow={buildUserDetailTableRow}
        />
      </div>
    </div>
  );
};
