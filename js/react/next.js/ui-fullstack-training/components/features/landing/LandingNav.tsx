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
            <button><Header icon='mdi:register' label='Register' /></button>
            <Link href={'/tasks'}><Header icon='ant-design:login-outlined' label='Login' /></Link>
        </div>
    </div>)
}
