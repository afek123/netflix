const multer = require('multer');
const path = require('path');

// Set storage engine
const storage = multer.diskStorage({
  destination: './uploads/',  // Destination folder for uploads
  filename: (req, file, cb) => {
    cb(null, `${file.fieldname}-${Date.now()}${path.extname(file.originalname)}`);  // Unique filename
  },
});

// Check file type (both image and video)
function checkFileType(file, cb) {
  console.log('Uploaded file:', file.originalname, 'MIME type:', file.mimetype);

  // Allowed image extensions
  const imageFileTypes = /jpeg|jpg|png|gif/;
  // Allowed video extensions
  const videoFileTypes = /mp4|avi|mkv|mov/;

  // Get the file extension
  const extname = path.extname(file.originalname).toLowerCase();

  // Check if file is an image
  const isImage = imageFileTypes.test(extname) && file.mimetype.startsWith('image/');

  // Check if file is a video
  const isVideo = videoFileTypes.test(extname) && (file.mimetype.startsWith('video/') || file.mimetype === 'video/*');

  if (isImage || isVideo) {
    return cb(null, true);  // Accept the file
  } else {
    cb('Error: Only images and videos are allowed!');  // Reject if not an image or video
  }
}

// Separate size limits for video and poster files
const uploadFields = multer({
  storage: storage,
  fileFilter: (req, file, cb) => {
    checkFileType(file, cb);
  },
}).fields([
  {
    name: 'videoUrl',
    maxCount: 1,
    limits: { fileSize: 50000000 },  // 50MB max for video file
  },
  {
    name: 'posterUrl',
    maxCount: 1,
    limits: { fileSize: 2000000 },  // 2MB max for poster file
  }
]);

// Export the configured multer middleware
module.exports = uploadFields;