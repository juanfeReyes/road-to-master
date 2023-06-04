import { Bank } from "@/model/Bank";
import { useTranslation } from "next-i18next";
import Image from "next/image";
import { DynamicTable } from "../shared/DynamicTable";
import { User } from "@/model/User";
import { useRouter } from "next/router";
import {
  EnvelopeIcon,
  PhoneIcon,
  MapPinIcon,
} from "@heroicons/react/24/outline";

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
      <tr key={row.id} onClick={() => navigateToUserDetailPage(row)} className="odd:bg-white even:bg-slate-50 hover:bg-blue-400">
        <td className="px-2 py-1 truncate">{row.name}</td>
        <td className="px-2 py-1 truncate">{row.email}</td>
        <td className="px-2 py-1 truncate">{row.location}</td>
        <td className="px-2 py-1 truncate">{row.state}</td>
      </tr>
    );
  };

  const userDetailHeaders = [t("labels.name"), t("labels.email"), t("labels.location"), t("labels.state")];

  return (
    <div>
      <div className="flex items-start gap-8 p-4">
        <div className="flex-grow-0 min-w-min">
          <Image
            className="rounded-3xl"
            src={bank.imageUrl}
            alt={"Picture for bank"}
            width={500}
            height={500}
          />
        </div>
        <div className="flex flex-col gap-6">
          <h1 className="text-4xl">{bank.name}</h1>
          <div>
            <p>{bank.description}</p>
          </div>
          <div className="flex flex-col gap-4">
            <div>
              <p>{t("labels.contactInformation")}</p>
              <p className="w-full flex gap-2">
                <EnvelopeIcon className="w-5 stroke-current text-gray-600" />
                {bank.contact_information}
              </p>
              <p className="w-full flex gap-2 content-start">
                <div>
                  <MapPinIcon className="w-5 stroke-current text-gray-600" />
                </div>
                <div>
                  {bank.country}, {bank.city}
                  <p className="w-full">{bank.address}</p>
                </div>
              </p>
            </div>
            <div>
              <p>{t("labels.managerInformation")}</p>
              <p className="w-full">{bank.expand.manager_user.name}</p>
              <p className="w-full flex gap-2">
                <EnvelopeIcon className="w-5 stroke-current text-gray-600" />
                {bank.expand.manager_user.email}
              </p>
              <p className="w-full flex gap-2">
                <PhoneIcon className="w-5 stroke-current text-gray-600" />
                {bank.expand.manager_user.phone}
              </p>
            </div>
          </div>
        </div>
      </div>
      <div className="p-4">
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
