import { useEffect, useRef, useState } from 'react'
import type { RefObject } from 'react'

const useIntersectionObsever = (targetRef: RefObject<HTMLElement>) => {
  const [isInViewport, setIsInViewport] = useState(false)
  const observer = useRef<IntersectionObserver>()

  useEffect(() => {
    if (!observer.current) {
      const observerCallback = (entries: IntersectionObserverEntry[]) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            setIsInViewport(true)
          }
        })
      }

      observer.current = new window.IntersectionObserver(observerCallback, {
        threshold: 0,
      })
    }

    if (targetRef.current) {
      observer.current.observe(targetRef.current)
    }

    return () => {
      if (observer.current) {
        observer.current.disconnect()
      }
    }
  }, [targetRef])

  return isInViewport
}

export default useIntersectionObsever
