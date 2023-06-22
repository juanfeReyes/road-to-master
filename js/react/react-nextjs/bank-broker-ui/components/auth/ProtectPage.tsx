import { useSession } from "next-auth/react";
import { useRouter } from "next/router";
import { useEffect } from "react";

interface ProtectPageProps {
  children: JSX.Element;
}

export const ProtectPage = ({children}: ProtectPageProps) => {
  const { status: sessionStatus } = useSession();
  const authorized = sessionStatus === 'authenticated';
  const unAuthorized = sessionStatus === 'unauthenticated';
  const loading = sessionStatus === 'loading';
  const router = useRouter();
  
  useEffect(() => {
    if (loading || !router.isReady) return;


    if (unAuthorized) {
      router.push('/auth/signin')
    }
  }, [loading, unAuthorized, sessionStatus, router])

  if (loading) {
    return <>Loading app...</>;
  }

  return ( authorized ?
    <>
     {children}
    </>
    : <>Validating Authentication</>
  );
}