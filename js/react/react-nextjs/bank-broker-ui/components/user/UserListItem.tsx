import { User } from "@/model/User";
import { useTranslation } from "next-i18next";

interface UserListItemProps {
  user: User;
}

export const UserListItem = ({ user }: UserListItemProps) => {
  const { t } = useTranslation("common");

  return (
    <div className="flex items-center odd:bg-white even:bg-slate-50 hover:bg-gray-300">
      <div>
        <div className="flex items-center">
          <div className="font-bold pr-3">{t("labels.name")}: </div>
          <h3>{user.name}</h3>
        </div>
        <div className="flex items-center">
          <div className="font-bold pr-3">{t("labels.email")}: </div>
          <p>{user.email}</p>
        </div>
      </div>
    </div>
  );
};
