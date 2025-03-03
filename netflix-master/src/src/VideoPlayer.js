import React from 'react';
import { useParams } from 'react-router-dom';

const VideoPlayer = () => {
  const { movieId } = useParams();

  return (
    <div>
      <h1>Playing Movie {movieId}</h1>
      {/* You can embed a video player here */}
      <video controls>
        <source src={`http://foo.com/api/videos/${movieId}`} type="video/mp4" />
        Your browser does not support the video tag.
      </video>
    </div>
  );
};

export default VideoPlayer;
