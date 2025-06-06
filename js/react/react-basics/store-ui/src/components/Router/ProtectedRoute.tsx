import { useEffect } from "react";
import { useAuth } from "../../services/hooks/AuthHooks"
import { useRouter } from "next/router";

export const ProtectedRoute = ({ children }: { children: JSX.Element }) => {
  const [user] = useAuth()
  const router = useRouter();

  useEffect(() => {
    if (!user) {
      router.push('/login')
    }
  })

  

  return user? children: <>Loading....</>;
}
