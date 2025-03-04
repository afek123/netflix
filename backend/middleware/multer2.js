const multer = require('multer');
const path = require('path');

// Set storage engine
const storage = multer.diskStorage({
  destination: './profilePictures/',  // Destination folder for uploads
  filename: (req, file, cb) => {
    cb(null, `${file.fieldname}-${Date.now()}${path.extname(file.originalname)}`);  // Unique filename
  },
});

// Check file type (only images allowed)
function checkFileType(file, cb) {
  console.log('Uploaded file:', file.originalname, 'MIME type:', file.mimetype);

  // Allowed image extensions
  const imageFileTypes = /jpeg|jpg|png|gif/;

  // Get the file extension
  const extname = path.extname(file.originalname).toLowerCase();

  // Check if file is an image
  const isImage = imageFileTypes.test(extname) && file.mimetype.startsWith('image/');

  if (isImage) {
    return cb(null, true);  // Accept the file
  } else {
    cb('Error: Only images are allowed!');  // Reject if not an image
  }
}

// Initialize upload
const upload = multer({
  storage: storage,
  limits: { fileSize: 2000000 },  // 2MB max for image file
  fileFilter: (req, file, cb) => {
    checkFileType(file, cb);
  },
});

// Export the configured multer middleware
const uploadFields = upload.fields([{ name: 'picture', maxCount: 1 }]);
module.exports = uploadFields;