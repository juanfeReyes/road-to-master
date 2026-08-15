import Image from 'next/image';
import logoImage from '@/public/favicon.png'
import { Header } from '@/components/common/layout/Header';
import Link from 'next/link';

export const LandingNav = () => {

    return (<div className='p-5 flex items-center justify-between'>
        <Image
            className='w-10'
            src={logoImage}
            alt='logo'
            placeholder='blur' />

        <div>
            <ul className='flex gap-10 font-bold'>
                <li>How it works</li>
                <li>Use cases</li>
                <li>Features</li>
                <li>FAQ</li>
            </ul>
        </div>
        <div className='flex gap-3 mr-5'>
            <Link href={'/sign-up'}><Header icon='mdi:register' label='Sign Up' /></Link>
            <Link href={'/sign-in'}><Header icon='ant-design:login-outlined' label='Sign In' /></Link>
        </div>
    </div>)
}
