import Link from "next/link";
import { useTranslation } from "next-i18next";
import { BanknotesIcon, Bars4Icon } from "@heroicons/react/24/outline";
import { signIn } from 'next-auth/react'

interface NavigationBarProps {
  children: JSX.Element;
}

export const NavigationBar = ({ children }: NavigationBarProps) => {
  const { t } = useTranslation("common");

  return (
    <div>
      <div className="flex justify-between p-6 bg-gradient-to-r from-gray-300 to-blue-500">
        <div className="flex items-center space-x-4">
          <p>
            <BanknotesIcon className="w-6" />
          </p>
          <h1>
            <Link className="text-3xl" href="/">
              Bank Broker Manager
            </Link>
          </h1>
        </div>

        <div className="flex items-center space-x-4">
          <p>Github Icon</p>
          <button onClick={() => signIn()}>{t("labels.login")}</button>
          <div>
            <Bars4Icon className="w-6" />
          </div>
        </div>
      </div>
      <div className="p-4">{children}</div>
    </div>
  );
};
