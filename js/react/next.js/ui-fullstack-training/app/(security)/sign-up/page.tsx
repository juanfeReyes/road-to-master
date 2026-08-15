import { SignUpForm } from "@/components/features/auth/SignUpForm";

export default async function RegisterPage() {
    return (
        
        <div className="flex justify-center items-center h-screen bg-linear-to-br from-cyan-500 to-indigo-500">
            <SignUpForm />
        </div>
    )
}
