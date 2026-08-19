import { SignInForm } from "@/components/features/auth/signInForm/SignInForm";

export default async function RegisterPage() {
    return (

        <div className="flex justify-center items-center h-screen bg-linear-to-br from-cyan-500 to-indigo-500">
            <SignInForm />
        </div>
    )
}