import { Transaction } from "@/model/Transaction";
import { User } from "@/model/User";
import { useEffect, useState } from "react";
import { DynamicTable } from "../shared/DynamicTable";
import { useTranslation } from "next-i18next";
import {
  EnvelopeIcon,
  MapPinIcon,
  PhoneIcon,
  UserIcon,
} from "@heroicons/react/24/outline";
import Image from "next/image";

interface UserDetailProps {
  user: User;
  deposits: Transaction[];
  withdrawals: Transaction[];
  userAvatarUrl: string;
}

export const UserDetail = ({
  user,
  deposits,
  withdrawals,
  userAvatarUrl,
}: UserDetailProps) => {
  const { t } = useTranslation("common");
  const [transactions, setTransactions] = useState<Transaction[]>([]);

  useEffect(() => {
    const joinTransactions = deposits.concat(withdrawals);
    joinTransactions.sort((a: Transaction, b: Transaction) => {
      return new Date(b.date).getTime() - new Date(a.date).getTime();
    });
    setTransactions(joinTransactions);
  }, []);

  const transactionHeaders = [
    t("labels.origin"),
    t("labels.destination"),
    t("labels.amount"),
    t("labels.date"),
  ];

  const buildUserDetailTableRow = (row: Transaction) => {
    const formatter = Intl.NumberFormat('en-US', {style: 'currency', currency: 'USD'})
    return (
      <tr className="odd:bg-white even:bg-slate-50 hover:bg-blue-400">
        <td className="px-2 py-1 truncate">{row.expand.origin.name}</td>
        <td className="px-2 py-1 truncate">{row.expand.destination.name}</td>
        <td className="px-2 py-1 truncate">{formatter.format(row.amount)}</td>
        <td className="px-2 py-1 truncate">{new Date(row.date).toDateString()}</td>
      </tr>
    );
  };

  return (
    <>
      <div className="flex items-center gap-3">
        <Image
          src={userAvatarUrl}
          alt="user avatar image"
          width={250}
          height={250}
        />
        <div>
          <h2 className="text-2xl pb-2">{user.name}</h2>
          <p className="flex gap-1">
            <UserIcon className="w-5" />
            {user.username} ({user.state})
          </p>
          <p className="flex gap-1">
            <EnvelopeIcon className="w-5 stroke-current text-gray-600" />
            {user.email}
          </p>
          <p className="flex gap-1">
            <PhoneIcon className="w-5 stroke-current text-gray-600" />
            {user.phone}
          </p>
          <p className="flex gap-1">
            <MapPinIcon className="w-5 stroke-current text-gray-600" />
            {user.location}
          </p>
        </div>
      </div>
      <div>
        <DynamicTable
          tableTitle={t("labels.transactions.title")}
          headers={transactionHeaders}
          body={transactions}
          buildBodyRow={buildUserDetailTableRow}
        />
      </div>
    </>
  );
};
