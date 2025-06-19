'use no memo'

import { useState, type JSX } from "react"
import './ProfilerBox.css'
import { IoTimerOutline } from "react-icons/io5";
import afterFrame from "afterframe";


interface ProfilerBoxProps {
  id: string
  children: (counter: number) => JSX.Element
}
export const ProfilerBox = ({ children, id }: ProfilerBoxProps) => {

  const [counter, setCounter] = useState(0)
  const [renderTime, setRenderTime] = useState(0);

  const increaseCounter = () => {
    const interaction = measureInteraction(id);
    setCounter(counter + 1)
    afterFrame(() => {
      const interactionTime = interaction.end();
      setRenderTime(interactionTime)
    });
  }

  return (<div className="vertical box_border">
    <div id={`${id}-time`} className="horizontal justify_between">
      <div>
        renders: {counter}
      </div>
      <div className="horizontal align_center">
        <IoTimerOutline />{`${renderTime.toFixed(3)}ms`}
      </div>
    </div>
    <div>
      {children(counter)}
      <button onClick={increaseCounter}>Render</button>
    </div>
  </div>)
}

function measureInteraction(interactionName: string) {
  performance.mark(interactionName + ' start');

  return {
    end() {
      performance.mark(interactionName + ' end');
      const measure = performance.measure(
        interactionName + ' duration',
        interactionName + ' start',
        interactionName + ' end',
      );
      console.log('The interaction took', measure.duration, 'ms');
      return measure.duration
    },
  };
}

