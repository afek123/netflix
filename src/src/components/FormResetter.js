import { useEffect } from 'react';

const FormResetter = ({ setTitle, setDirector, setCategory, setVideoUrl, setPosterUrl, setErrorMessage, setSelectedMovie }) => {
  useEffect(() => {
    setTitle("");
    setDirector("");
    setCategory("");
    setVideoUrl("");
    setPosterUrl("");
    setErrorMessage("");
    setSelectedMovie(null);
  }, [setTitle, setDirector, setCategory, setVideoUrl, setPosterUrl, setErrorMessage, setSelectedMovie]);

  return null; // This is a functional component, it only resets form state and does not render anything
};

export default FormResetter;
