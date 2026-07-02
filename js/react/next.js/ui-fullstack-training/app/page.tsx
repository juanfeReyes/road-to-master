import { FlipCard } from "@/components/features/landing/FlipCard";
import { LandingNav } from "@/components/features/landing/LandingNav";
import howItWorks from '@/public/HowItWorks.gif'
import TableViewImg from '@/public/TableView.png'
import createTasksImg from '@/public/CreateTasks.png'
import Image from 'next/image';


export default function Home() {
  return (
    <div >
      <LandingNav />
      <div className="p-7 flex flex-col gap-5">
        <div className="flex justify-evenly">
          <div className="flex flex-col justify-center ">
            <div className="flex flex-col font-bold text-8xl">
              <p>Plan.</p>
              <p>Manage.</p>
              <p>Success.</p>
            </div>
          </div>
          <Image
            className='w-1/2 p-6 bg-white/30 backdrop-blur-md border border-white/20 rounded-xl shadow-lg'
            src={howItWorks}
            alt='logo' />
        </div>
        <div className="flex justify-evenly items-center bg-blue-800 rounded-3xl ">
          <div className="flex justify-center gap-5">
            <FlipCard
              front={<Image
                src={TableViewImg}
                alt="tableView"
              />}
              back={<div className="p-2 flex flex-col gap-5">
                <h2 className="font-bold text-2xl text-center">Big picture</h2>
                <p className="">View all your task, prioritize and manage deadlines.</p>
              </div>}
            />
            <FlipCard
              front={<Image
                className="w-50 m-auto"
                src={createTasksImg}
                alt="tableView"
              />}
              back={<div className="p-2 flex flex-col gap-5">
                <h2 className="font-bold text-2xl text-center">Plan Ahead</h2>
                <p className="">Create all your task, add all the details and define your deadlines to plan for the future</p>
              </div>}
            />
          </div>
          <h1 className=" w-1/3 font-bold text-6xl text-white text-right">Advanced Features</h1>
        </div>
      </div>
    </div>
  );
}
