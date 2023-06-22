import "@/styles/globals.css";
import type { AppProps } from "next/app";
import { appWithTranslation } from "next-i18next";
import "../styles/globals.css";
import { SessionProvider } from "next-auth/react";
import '../styles/globals.css'

const App = ({ Component, pageProps: {session, ...pageProps} }: AppProps) => {
  return (
    <SessionProvider session={session}>
      <Component {...pageProps} />
    </SessionProvider>
  );
};

export default appWithTranslation(App);
