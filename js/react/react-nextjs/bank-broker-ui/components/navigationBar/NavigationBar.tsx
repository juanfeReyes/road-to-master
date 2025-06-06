import Link from "next/link";
import { useTranslation } from "next-i18next";
import {
  ArrowLeftOnRectangleIcon,
  ArrowRightOnRectangleIcon,
  BanknotesIcon,
  Bars4Icon,
} from "@heroicons/react/24/outline";
import { signIn, signOut, useSession } from "next-auth/react";

interface NavigationBarProps {
  children: JSX.Element;
}

export const NavigationBar = ({ children }: NavigationBarProps) => {
  const { t } = useTranslation("common");
  const { data: session } = useSession();

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
          {session ? (
            <button
              className="flex gap-1 items-center"
              onClick={() => signOut()}
            >
              {t("labels.logout")}
              <ArrowLeftOnRectangleIcon className="w-4" />
            </button>
          ) : (
            <Link className="flex gap-1 items-center" href={"/auth/signin"}>
              {t("labels.login")}
              <ArrowRightOnRectangleIcon className="w-4" />
            </Link>
          )}
          <div>
            <Bars4Icon className="w-6" />
          </div>
        </div>
      </div>
      <div className="p-4">{children}</div>
    </div>
  );
};
