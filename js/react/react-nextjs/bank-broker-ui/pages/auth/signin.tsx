import type {
  GetServerSidePropsContext,
  InferGetServerSidePropsType,
} from "next";
import { getServerSession } from "next-auth/next";
import { authOptions } from "../api/auth/[...nextauth]";
import { getProviders, signIn } from "next-auth/react";
import BanknotesIcon from "@heroicons/react/24/outline/BanknotesIcon";
import Link from "next/link";
import {FaGithub} from 'react-icons/fa'

interface ProviderButtonProps {
  providerId: any;
  providerName: any;
}

const ProviderButton = ({ providerId, providerName }: ProviderButtonProps) => {
  return (
    <div key={providerName} className="p-1 hover:bg-slate-100 rounded-md hover:font-bold">
      <button className="flex items-center space-x-3" onClick={() => signIn(providerId)}>
        <p>Sign in with {providerName}</p> <FaGithub/>
      </button>
    </div>
  );
};

export default function SingIn({
  providers,
}: InferGetServerSidePropsType<typeof getServerSideProps>) {
  return (
    <div className="flex flex-col items-center justify-center bg-gradient-to-r from-gray-300 to-blue-500 h-full">
      <div className="bg-white p-4 rounded-md shadow-2xl flex flex-col items-center justify-center">
        <div className="flex items-center space-x-4 pb-3">
          <p>
            <BanknotesIcon className="w-6" />
          </p>
          <h1>
            <Link className="text-3xl" href="/">
              Bank Broker Manager
            </Link>
          </h1>
        </div>
        {Object.values(providers).map((provider) => (
          <ProviderButton
            providerName={provider.name}
            providerId={provider.id}
          />
        ))}
      </div>
    </div>
  );
}

export async function getServerSideProps(context: GetServerSidePropsContext) {
  const session = await getServerSession(context.req, context.res, authOptions);

  if (session) {
    return { redirect: { destination: "/" } };
  }

  const providers = await getProviders();

  return {
    props: { providers: providers ?? [] },
  };
}
