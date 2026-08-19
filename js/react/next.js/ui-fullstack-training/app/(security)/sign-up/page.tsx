import { SignUpForm } from "@/components/features/auth/signUpForm/SignUpForm";

export default async function RegisterPage() {
    return (
        
        <div className="flex justify-center items-center h-screen bg-linear-to-br from-amber-500 to-rose-500">
            <SignUpForm />
        </div>
    )
}
