const multer = require('multer');
const path = require('path');

// Set storage engine
const storage = multer.diskStorage({
  destination: './uploads/',  // Destination folder for uploads
  filename: (req, file, cb) => {
    cb(null, `${file.fieldname}-${Date.now()}${path.extname(file.originalname)}`);  // Unique filename
  },
});

// Initialize upload
const upload = multer({
  storage: storage,
  limits: { fileSize: 10000000 },  // Limit for total size (can also be specified per file)
  fileFilter: (req, file, cb) => {
    checkFileType(file, cb);
  },
});

// Check file type (both image and video)
function checkFileType(file, cb) {
  // Allowed image extensions
  const imageFileTypes = /jpeg|jpg|png|gif/;
  // Allowed video extensions
  const videoFileTypes = /mp4|avi|mkv|mov/;

  // Check if file is an image
  const isImage = imageFileTypes.test(path.extname(file.originalname).toLowerCase()) && imageFileTypes.test(file.mimetype);

  // Check if file is a video
  const isVideo = videoFileTypes.test(path.extname(file.originalname).toLowerCase()) && videoFileTypes.test(file.mimetype);

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
